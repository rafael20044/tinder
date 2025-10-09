import { createClient } from '@supabase/supabase-js'
import { environment } from 'src/environments/environment.prod'

const supabaseUrl = 'https://uzqdwkpfhirjaxikxcch.supabase.co'
const supabaseKey = environment.SUPABASE_KEY
export const supabase = createClient(supabaseUrl, supabaseKey)